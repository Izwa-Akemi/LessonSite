package lesson.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lesson.com.model.dao.StudentDao;
import lesson.com.model.entity.StudentEntity;

@Service
public class StudentService {
	@Autowired
	private StudentDao studentDao;
	
	//ログイン処理
		public StudentEntity findByStudentEmailAndPassword(String studentEmail, String studentPassword) {
			//コントローラークラスからadminNameとpasswordと受け取って結果を受け取る
			List<StudentEntity> studentList = studentDao.findByStudentEmailAndStudentPassword(studentEmail, studentPassword);
			//もしadminListが空だった場合には、nullを返す処理
		    if(studentList.isEmpty()){
		        return null;
		    }else{
		    	//もしstudentListが空でなかった場合には、Ｌｉｓｔの0番目の要素を取得する
		        return studentList.get(0);
		    }

		}
		
		//保存処理
		public boolean createAccount(String studentName,String studentEmail, String studentPassword) {
			//StudentRegisterControllerから渡される生徒情報（studentEmail）を条件にDB検索で検索する
			StudentEntity studentEntity = studentDao.findByStudentEmail(studentEmail);
			//RegisterControllerから渡されるユーザ情報（studentEmail、studentPassword）を条件にDB検索で検索した結果
			//なかった場合には、保存
			if (studentEntity==null) {
				studentDao.save(new StudentEntity(studentName,studentEmail, studentPassword));
				return true;
			} else {
				return false;
			}
		}
}
