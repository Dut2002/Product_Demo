package com.example.demo_oracle_db.service.mySupplier.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Approval;
import com.example.demo_oracle_db.entity.Supplier;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.email.EmailService;
import com.example.demo_oracle_db.service.mySupplier.SupplierService;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.request.RequestFilter;
import com.example.demo_oracle_db.service.mySupplier.request.SaveNoteRequest;
import com.example.demo_oracle_db.service.mySupplier.request.SupplierFilter;
import com.example.demo_oracle_db.service.mySupplier.response.RequestDto;
import com.example.demo_oracle_db.service.mySupplier.response.SupplierInfo;
import com.example.demo_oracle_db.service.supplier.mySupplier.request.OpenSupplierRequest;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Page<RequestDto> viewRequest(RequestFilter filter) {
        int pageNum = filter.getPageNum() != null ? filter.getPageNum() : 1;
        int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 8;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RequestDto> query = cb.createQuery(RequestDto.class);
        Root<Approval> root = query.from(Approval.class);

        // Join với bảng Account để lấy username của requester
        Join<Approval, Account> requesterJoin = root.join("requester", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }
        if(filter.getCreatedStart() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedStart()));
        }
        if(filter.getCreatedEnd() != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedStart()));
        }
        if(filter.getUpdatedStart() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), filter.getCreatedStart()));
        }
        if(filter.getUpdatedEnd() != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), filter.getCreatedStart()));
        }
        if(filter.getNote()!=null && !filter.getNote().isBlank()){
            String search = "%" + filter.getNote().trim().toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("note")), search));
        }

        if(filter.getRequesterId()!= null){
            predicates.add(cb.equal(requesterJoin.get("id"), filter.getRequesterId()));
        }

        // Lọc theo loại approvalType (ví dụ: SUPPLIER_REGISTRATION)
        query.where(cb.equal(root.get("approvalType"), Constants.ApprovalType.SUPPLIER_REGISTRATION));
        // Lựa chọn các trường để truy vấn (username của requester)
        query.multiselect(
                root.get("id"),
                root.get("data"),
                root.get("approvalType"),
                root.get("status"),
                root.get("requesterId"),
                requesterJoin.get("fullName"),
                root.get("createdAt"),
                root.get("updatedAt"),
                root.get("note")
        );
        query.orderBy(
                cb.desc(cb.selectCase()
                        .when(cb.equal(root.get("status"), "Pending"), 2)
                        .otherwise(1)),
                // Sau đó sắp xếp theo updateAt
                cb.desc(root.get("updatedAt")),
                // Cuối cùng sắp xếp theo createAt
                cb.desc(root.get("createdAt"))
        );

        List<RequestDto> list = entityManager.createQuery(query)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Approval> approvalRoot = countQuery.from(Approval.class);
        countQuery.select(cb.count(approvalRoot));
        countQuery.where(cb.and(predicates.toArray(Predicate[]::new)));
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<RequestDto>(list, PageRequest.of(pageNum - 1, pageSize), totalRecords);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            approvalRepository.processRequest(request.getId(), request.getStatus().name(), account.getId(), LocalDateTime.now());
            CompletableFuture.supplyAsync(() -> {
                try {
                    Account requester = accountRepository.findById(approval.getRequesterId()).orElseThrow(()-> new DodException(MessageCode.USER_NOT_FOUND));

                    emailService.sendSimpleMail(
                            requester.getEmail(),
                            "Yêu cầu đăng ký làm Supplier",
                            requester.getFullName(),
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
            approvalRepository.processRequest(request.getId(), request.getStatus().name(), account.getId(), LocalDateTime.now());

            CompletableFuture.supplyAsync(() -> {
                try {
                    Account requester = accountRepository.findById(approval.getRequesterId()).orElseThrow(()-> new DodException(MessageCode.USER_NOT_FOUND));

                    emailService.sendSimpleMail(
                            requester.getEmail(),
                            "Yêu cầu đăng ký làm Supplier",
                            requester.getFullName(),
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

    @Override
    public Page<SupplierInfo> viewSupplier(SupplierFilter filter) {
        int pageNum = filter.getPageNum() != null ? filter.getPageNum() : 1;
        int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 8;


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SupplierInfo> query = criteriaBuilder.createQuery(SupplierInfo.class);
        Root<Supplier> root = query.from(Supplier.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            String search = "%" + filter.getName().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), search));
        }
        if (filter.getAddress() != null && !filter.getAddress().isBlank()) {
            String search = "%" + filter.getAddress().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), search));
        }
        if (filter.getContact() != null && !filter.getContact().isBlank()) {
            String search = "%" + filter.getContact().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("contact")), search));
        }
        if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
            String search = "%" + filter.getPhone().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), search));
        }
        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            String search = "%" + filter.getEmail().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), search));
        }
        if (filter.getWebsite() != null && !filter.getWebsite().isBlank()) {
            String search = "%" + filter.getWebsite().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("website")), search));
        }

        query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));

        query.multiselect(
                root.get("id"),
                root.get("name"),
                root.get("contact"),
                root.get("address"),
                root.get("phone"),
                root.get("email"),
                root.get("website")
        );

        List<SupplierInfo> list = entityManager.createQuery(query)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Supplier> supplierRoot = countQuery.from(Supplier.class);
        countQuery.select(criteriaBuilder.count(supplierRoot));
        countQuery.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));
        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();


        return new PageImpl<SupplierInfo>(list, PageRequest.of(pageNum - 1, pageSize), totalRecords);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNote(SaveNoteRequest request) throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        if(!approvalRepository.existsById(request.getId())){
            throw  new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        approvalRepository.saveNote(request.getId(), request.getNote(), account.getId(), LocalDate.now());
    }
}
