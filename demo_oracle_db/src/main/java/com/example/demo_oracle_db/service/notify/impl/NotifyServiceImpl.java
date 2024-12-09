package com.example.demo_oracle_db.service.notify.impl;

import com.example.demo_oracle_db.entity.Notify;
import com.example.demo_oracle_db.entity.NotifyAccount;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.NotifyAccountRepository;
import com.example.demo_oracle_db.repository.NotifyRepository;
import com.example.demo_oracle_db.service.notify.NotifyService;
import com.example.demo_oracle_db.service.notify.req.NotifyReadReq;
import com.example.demo_oracle_db.service.notify.req.NotifyRequest;
import com.example.demo_oracle_db.service.notify.res.NotifyDto;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    NotifyRepository notifyRepository;
    @Autowired
    NotifyAccountRepository notifyAccountRepository;
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<NotifyDto> getNotify(NotifyRequest req) throws DodException {
        if (req.getPageNum() == null) {
            req.setPageNum(1);
        }
        if (req.getPageSize() == null) {
            req.setPageSize(8);
        }
        // get list
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotifyDto> query = cb.createQuery(NotifyDto.class);

        Root<Notify> root = query.from(Notify.class);
        Join<Notify, NotifyAccount> rootJoin = root.join("notifyAccountList", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        if (req.getPageRedirect() != null) {
            predicates.add(cb.equal(root.get("pageRedirect"), req.getPageRedirect()));
        }
        predicates.add(cb.equal(rootJoin.get("accountId"), accountId));
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        query.multiselect(
                root.get("id"),
                root.get("header"),
                root.get("message"),
                root.get("timestamp"),
                root.get("pageRedirect"),
                root.get("data"),
                rootJoin.get("isRead")
        );
// Sắp xếp isRead = false lên đầu và sau đó theo timestamp
        query.orderBy(
                cb.asc(rootJoin.get("isRead")),
                cb.desc(root.get("timestamp"))
        );
        List<NotifyDto> list = entityManager.createQuery(query)
                .setMaxResults(req.getPageSize())
                .setFirstResult((req.getPageNum() - 1) * req.getPageSize())
                .getResultList();
        //Get total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Notify> countRoot = countQuery.from(Notify.class);
        predicates = new ArrayList<>();
        Join<Notify, NotifyAccount> countRootJoin = countRoot.join("notifyAccountList", JoinType.INNER);
        if (req.getPageRedirect() != null) {
            predicates.add(cb.equal(root.get("pageRedirect"), req.getPageRedirect()));
        }
        predicates.add(cb.equal(countRootJoin.get("accountId"), accountId));
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        countQuery.select(cb.count(countRoot));

        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(list, PageRequest.of(req.getPageNum() - 1, req.getPageSize()), totalCount);
    }

    @Override
    public boolean hasUnreadNotify() throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        return notifyAccountRepository.existsByAccountIdAndIsRead(accountId, 0);
    }

    @Override
    public void checkRead(NotifyReadReq req) throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));

        List<Tuple> notifyAccount = notifyAccountRepository.findIdByNotifyIdAndAccountId(req.getNotifyId(), accountId);
        if (notifyAccount.isEmpty()) {
            throw new DodException(MessageCode.NOTIFY_NOT_FOUND);
        }
        if (notifyAccount.get(0).get("is_read", Integer.class) == 1) {
            throw new DodException(MessageCode.NOTIFY_ALREADY_READ);
        }
        notifyAccountRepository.setRead(notifyAccount.get(0).get("id", Long.class), 1);
    }

    @Override
    public void checkReadAll() throws DodException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long accountId = accountRepository.getIdByUserName(username).orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));

        notifyAccountRepository.setAllRead(accountId);
    }
}
