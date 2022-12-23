package lesson.com.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.AdminDao;
import lesson.com.model.entity.AdminEntity;

@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;
	
	
	//ログイン処理
	public AdminEntity findByAdminEmailAndPassword(String adminEmail, String adminPassword) {
		//コントローラークラスからadminEmailとpasswordと受け取って結果を受け取る
		List<AdminEntity> adminList = adminDao.findByAdminEmailAndAdminPassword(adminEmail, adminPassword);
		//もしadminListが空だった場合には、nullを返す処理
	    if(adminList.isEmpty()){
	        return null;
	    }else{
	    	//もしadminListが空でなかった場合には、Ｌｉｓｔの0番目の要素を取得する
	        return adminList.get(0);
	    }

	}
	
	//保存処理
	public boolean createAccount(String adminName,String adminEmail, String adminPassword) {
		LocalDateTime dateTimeNow = LocalDateTime.now();
		//AdminRegisterControllerから渡される管理者情報（メールアドレス）を条件にDB検索で検索する
		AdminEntity adminEntity = adminDao.findByAdminEmail(adminEmail);
		//AdminRegisterControllerから渡される管理者情報（メール、パスワード）を条件にDB検索で検索した結果
		//なかった場合には、保存
		if (adminEntity==null) {
			adminDao.save(new AdminEntity(adminName,adminEmail, adminPassword,dateTimeNow));
			return true;
		} else {
			return false;
		}
	}
	
}
