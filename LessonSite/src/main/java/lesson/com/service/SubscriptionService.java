package lesson.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.SubscriptionDao;
import lesson.com.model.entity.SubscriptionEntity;


@Service
public class SubscriptionService {
	@Autowired
	SubscriptionDao subscriptionDao;
	public List<SubscriptionEntity> getPurchaseHistory(Long studentId) {
		return subscriptionDao.findByStudentId(studentId);
	}
}
