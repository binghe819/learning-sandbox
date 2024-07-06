package com.binghe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElse(null);
    }
}
