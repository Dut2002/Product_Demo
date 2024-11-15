package com.example.demo_oracle_db.service.mySupplier.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Approval;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.email.EmailService;
import com.example.demo_oracle_db.service.mySupplier.SupplierService;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.response.RequestDto;
import com.example.demo_oracle_db.service.supplier.mySupplier.request.OpenSupplierRequest;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ApprovalRepository approvalRepository;
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    EmailService emailService;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RequestDto> viewRequest() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RequestDto> query = cb.createQuery(RequestDto.class);
        Root<Approval> root = query.from(Approval.class);

        // Join với bảng Account để lấy username của requester
        Join<Approval, Account> requesterJoin = root.join("requester", JoinType.INNER);

        // Lọc theo loại approvalType (ví dụ: SUPPLIER_REGISTRATION)
        query.where(cb.equal(root.get("approvalType"), Constants.ApprovalType.SUPPLIER_REGISTRATION));

        // Lựa chọn các trường để truy vấn (username của requester)
        query.select(
                cb.construct(
                        RequestDto.class,
                        root.get("id"),
                        root.get("data"),
                        root.get("approvalType"),
                        root.get("status"),
                        root.get("requesterId"),
                        requesterJoin.get("username"),
                        root.get("createdAt"),
                        root.get("updatedAt"),
                        root.get("note")
                )
        );
        query.orderBy(
                cb.desc(cb.selectCase()
                        .when(cb.equal(root.get("status"), "Pending"), 1)
                        .otherwise(2)),
                // Sau đó sắp xếp theo updateAt
                cb.desc(root.get("updatedAt")),
                // Cuối cùng sắp xếp theo createAt
                cb.desc(root.get("createdAt"))
        );
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public void processRequest(ProcessRequest request) throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        if (!request.getApprovalType().equals(Constants.ApprovalType.SUPPLIER_REGISTRATION)) {
            throw new DodException(MessageCode.PROCESS_REQUEST_ERROR);
        }
        Approval approval = approvalRepository.findById(request.getId()).orElseThrow(() -> new DodException(MessageCode.REQUEST_NOT_FOUND));
        if (!approval.getStatus().equals(Constants.ApprovalStatus.PENDING))
            throw new DodException(MessageCode.REQUEST_COMPLETED);
        OpenSupplierRequest data;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.readValue(approval.getData(), OpenSupplierRequest.class);
        } catch (Exception e) {
            throw new DodException(MessageCode.JSON_PARSE_ERROR);
        }
        if (request.getStatus().equals(Constants.ApprovalStatus.APPROVED)) {

            Long supplier = roleRepository.findIdByName(Constants.Role.SUPPLIER).orElseThrow(()->new DodException(MessageCode.ROLE_NOT_FOUND));
            if(!accountRoleRepository.existsByAccountIdAndRoleId(approval.getRequesterId(), supplier)){
                accountRoleRepository.addAccountRole(approval.getRequesterId(), supplier);
            }
            supplierRepository.addSupplier(
                    data.getName(),
                    data.getContact(),
                    data.getAddress(),
                    data.getPhone(),
                    data.getEmail(),
                    data.getWebsite(),
                    approval.getRequesterId()
            );
            approvalRepository.processRequest(request.getId(), request.getStatus().name(), request.getNote(), account.getId(), LocalDateTime.now());
            CompletableFuture.supplyAsync(() -> {
                try {
                    String name = accountRepository.getFullNameById(approval.getRequesterId()).orElseThrow(()-> new DodException(MessageCode.USER_NOT_FOUND));

                    emailService.sendSimpleMail(
                            account.getEmail(),
                            "Yêu cầu đăng ký làm Supplier",
                            name,
                            "Yêu cầu của bạn đã được duyệt",
                            "Yêu cầu đăng ký cho supplier " + data.getName() + " đã được chấp thuận. Bạn từ giờ đã là Supplier của chúng tôi",
                            null, 0);
                    System.out.println("Email đã được gửi thành công.");

                } catch (Exception e) {
                    System.err.println("Send Mail Failed");
                }
                return null;
            });
        } else if (request.getStatus().equals(Constants.ApprovalStatus.REJECTED)) {
            approvalRepository.processRequest(request.getId(), request.getStatus().name(), request.getNote(), account.getId(), LocalDateTime.now());
            CompletableFuture.supplyAsync(() -> {
                try {
                    String name = accountRepository.getFullNameById(approval.getRequesterId()).orElseThrow(()-> new DodException(MessageCode.USER_NOT_FOUND));

                    emailService.sendSimpleMail(
                            account.getEmail(),
                            "Yêu cầu đăng ký làm Supplier",
                            name,
                            "Yêu cầu của bạn đã bị từ chối",
                            "Yêu cầu đăng ký cho supplier " + data.getName() + "đã được chấp thuận. Bạn từ giờ đã là Supplier của chúng tôi",
                            null, 0);
                    System.out.println("Email đã được gửi thành công.");
                } catch (Exception e) {
                    System.err.println("Send Mail Failed");
                }
                return null;
            });
        } else {
            throw new DodException(MessageCode.PROCESS_REQUEST_ERROR);
        }
    }
}
