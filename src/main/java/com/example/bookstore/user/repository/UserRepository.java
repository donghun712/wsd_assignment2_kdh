package com.example.bookstore.user.repository;

import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 찾기 (로그인/회원가입에서 사용)
    Optional<User> findByEmail(String email);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // (선택) 관리자용 상태별 조회 - 지금 UserService에서는 안 써도 되고, 있어도 문제 없음
    Page<User> findByUserStatus(UserStatus status, Pageable pageable);
}
