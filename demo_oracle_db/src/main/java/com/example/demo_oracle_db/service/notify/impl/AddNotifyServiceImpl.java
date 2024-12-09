package com.example.demo_oracle_db.service.notify.impl;

import com.example.demo_oracle_db.entity.Notify;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.ApprovalRepository;
import com.example.demo_oracle_db.repository.NotifyAccountRepository;
import com.example.demo_oracle_db.repository.NotifyRepository;
import com.example.demo_oracle_db.service.notify.AddNotifyService;
import com.example.demo_oracle_db.service.notify.req.AddNotifyReq;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddNotifyServiceImpl implements AddNotifyService {

    private final AccountRepository accountRepository;
    private final NotifyRepository notifyRepository;
    private final NotifyAccountRepository notifyAccountRepository;
    private final ApprovalRepository approvalRepository;

    @Override
    @Transactional
    public void addNotify(AddNotifyReq notifyReq, List<Long> accountIds) throws DodException {

        if (accountIds == null || accountIds.isEmpty()) {
            throw new DodException(MessageCode.NOTIFY_RECEIVER_EMPTY);
        }

        notifyRepository.addNotify(
                notifyReq.getHeader(),
                notifyReq.getMessage(),
                LocalDateTime.now(),
                notifyReq.getPageRedirect().name(),
                notifyReq.getData()
        );

        Long notifyId = notifyRepository.getLastInsertedId();

        for (Long accountId : accountIds
        ) {
            if (!accountRepository.existsById(accountId)) {
                throw new DodException(MessageCode.USER_NOT_FOUND);
            }
            notifyAccountRepository.addNotifyAccount(notifyId, accountId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplierRequest() {
       Notify notify = notifyRepository.findByPageRedirect(Constants.PageRedirect.SupplierRequestManagement).orElse(null);

       Integer pendingRequest = approvalRepository.countRequest(Constants.ApprovalType.SUPPLIER_REGISTRATION.name(), Constants.ApprovalStatus.PENDING.name());
       if(notify == null){
           if(pendingRequest != 0 ){
               notifyRepository.addNotify(
                       "Has Supplier Register Request",
                       "Has " + pendingRequest + " supplier requests in pending!",
                       LocalDateTime.now(),
                       Constants.PageRedirect.SupplierRequestManagement.name(),
                       null);
               Long notifyId = notifyRepository.getLastInsertedId();
               sendForSupplierManager(notifyId);
           }
       }else{
           if(pendingRequest == 0){
               notifyAccountRepository.deleteByNotifyId(notify.getId());
           }else{
               notifyRepository.updateSupplierRegisterNotify(
                       notify.getId(),
                       "Has " + pendingRequest + " supplier requests in pending!",
                       LocalDateTime.now(),
                       Constants.PageRedirect.SupplierRequestManagement.name()
                       );
               sendForSupplierManager(notify.getId());
           }
       }

    }

    private void sendForSupplierManager(Long notifyId) {
        List<Long> supplierManagerIds = accountRepository.findByRoleName(Constants.Role.SUPPLIER_MANAGER);
        for (Long accountId: supplierManagerIds
             ) {
            List<Tuple> notifyAccount = notifyAccountRepository.findIdByNotifyIdAndAccountId(notifyId, accountId);
            if (notifyAccount.isEmpty()) {
                notifyAccountRepository.addNotifyAccount(notifyId, accountId);
            }else{
                notifyAccountRepository.setRead(notifyAccount.get(0).get("id", Long.class), 0);
            }
        }
    }
}
