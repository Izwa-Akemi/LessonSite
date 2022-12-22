package lesson.com.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lesson.com.model.entity.AdminEntity;

public interface AdminDao extends JpaRepository<AdminEntity, Long> {
	AdminEntity save(AdminEntity adminEntity);
	List<AdminEntity> findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);
	AdminEntity findByAdminEmail(String adminEmail);
}
