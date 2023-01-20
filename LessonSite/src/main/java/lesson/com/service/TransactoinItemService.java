package lesson.com.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.TransactionItemDao;

import lesson.com.model.entity.TransactionItemEntity;


@Service
public class TransactoinItemService {
	@Autowired
	private TransactionItemDao transactionItemDao;
	// 保存処理
			public void createTransactoinHistory(Long transactionId,Long lessonId) {
				transactionItemDao.save(new TransactionItemEntity(transactionId,lessonId));
			}
}
