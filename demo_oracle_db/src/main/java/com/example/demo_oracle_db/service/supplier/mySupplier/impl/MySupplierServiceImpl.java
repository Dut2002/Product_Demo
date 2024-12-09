package com.example.demo_oracle_db.service.supplier.mySupplier.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Approval;
import com.example.demo_oracle_db.entity.Supplier;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.ApprovalRepository;
import com.example.demo_oracle_db.repository.SupplierRepository;
import com.example.demo_oracle_db.service.mySupplier.request.CancelRequest;
import com.example.demo_oracle_db.service.mySupplier.response.MyRequestDto;
import com.example.demo_oracle_db.service.mySupplier.response.SupplierInfo;
import com.example.demo_oracle_db.service.notify.AddNotifyService;
import com.example.demo_oracle_db.service.notify.res.SendNotifyService;
import com.example.demo_oracle_db.service.supplier.mySupplier.MySupplierService;
import com.example.demo_oracle_db.service.supplier.mySupplier.request.OpenSupplierRequest;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MySupplierServiceImpl implements MySupplierService {

    private final AccountRepository accountRepository;
    private final ApprovalRepository approvalRepository;
    private final SupplierRepository supplierRepository;
    private final EntityManager entityManager;
    private final AddNotifyService addNotifyService;
    private final SendNotifyService sendNotifyService;

    @Override
    public void supplierRegister(OpenSupplierRequest request) throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));

        if (approvalRepository.exists((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("requesterId"), accountId),
                    criteriaBuilder.equal(root.get("approvalType"), Constants.ApprovalType.SUPPLIER_REGISTRATION),
                    criteriaBuilder.equal(root.get("status"), Constants.ApprovalStatus.PENDING)
            ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })) {
            throw new DodException(MessageCode.UNABLE_REQUEST);
        }
        if(supplierRepository.existsByAccountId(accountId)){
            throw new DodException(MessageCode.SUPPLIER_ALREADY_REGISTER);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(request);  // Chuyển đổi đối tượng request thành chuỗi JSON
            approvalRepository.addRequest(
                    Constants.ApprovalType.SUPPLIER_REGISTRATION.name(),
                    accountId,
                    Constants.ApprovalStatus.PENDING.name(),
                    jsonRequest,//json
                    LocalDateTime.now()
            );
            CompletableFuture.supplyAsync(()->{
                try {
                    sendNotification();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                return null;
            });
        } catch (JsonProcessingException e) {
            throw new DodException(MessageCode.JSON_PARSE_ERROR);
        } catch (Exception e) {
            // Xử lý các lỗi khác (lỗi không xác định)
            throw new DodException(MessageCode.ADD_REQUEST_FAILED);
        }
    }

    private void sendNotification() {
        addNotifyService.addSupplierRequest();
        sendNotifyService.sendGroupNotification(Constants.Role.SUPPLIER_MANAGER, "New Notify");
    }

    @Override
    public List<MyRequestDto> mySupplierRequest() throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MyRequestDto> query = cb.createQuery(MyRequestDto.class);
        Root<Approval> root = query.from(Approval.class);

        query.where(cb.and(
                cb.equal(root.get("requesterId"), accountId),
                cb.equal(root.get("approvalType"), Constants.ApprovalType.SUPPLIER_REGISTRATION)
        ));
        query.multiselect(
                root.get("id"),
                root.get("data"),
                root.get("status"),
                root.get("createdAt"),
                root.get("updatedAt"),
                root.get("note")
        );
        query.orderBy(
                cb.desc(cb.selectCase()
                        .when(cb.equal(root.get("status"), Constants.ApprovalStatus.PENDING), 2)
                        .otherwise(1)),
                // Sau đó sắp xếp theo updateAt
                cb.desc(root.get("updatedAt")),
                // Cuối cùng sắp xếp theo createAt
                cb.desc(root.get("createdAt"))
        );


        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public SupplierInfo viewSupplerInfo() throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierInfo> query = cb.createQuery(SupplierInfo.class);
        Root<Supplier> root = query.from(Supplier.class);
        query.where(cb.equal(root.get("accountId"), accountId));
        query.multiselect(
                root.get("id"),
                root.get("name"),
                root.get("contact"),
                root.get("address"),
                root.get("phone"),
                root.get("email"),
                root.get("website")
        );
        return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void cancelRequest(CancelRequest request) throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));

        Approval approval = approvalRepository.findById(request.getId()).orElseThrow(() -> new DodException(MessageCode.REQUEST_NOT_FOUND));
        if (!approval.getRequesterId().equals(accountId)) {
            throw new DodException(MessageCode.REQUESTER_NOT_MATCH);
        }
        if (!approval.getStatus().equals(Constants.ApprovalStatus.PENDING)) {
            throw new DodException(MessageCode.REQUEST_COMPLETED);
        }
        approvalRepository.processRequest(approval.getId(), Constants.ApprovalStatus.CANCELED.name(), accountId, LocalDateTime.now());
        CompletableFuture.supplyAsync(()->{
            try {
                sendNotification();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        });
    }
}
