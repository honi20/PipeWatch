-- enterprise
INSERT INTO enterprise(enterprise_id, name, industry, manager_email, manager_phone_number, is_active)
VALUES(1L, 'paori', '제조업', 'admin@paori.com', '010-1234-5678', true);

-- user
INSERT INTO "user"(user_id, email, password, name, state, role, uuid)
VALUES(123L, 'test@ssafy.com', 'ssafy1234', '테스트', 0, 0, 'asdkljf;alksjdf');

-- employeeInfo
INSERT INTO employee_info(employee_info_id, emp_no, department, emp_class, user_id, enterprise_id)
VALUES(123L, 1243242L, 'IT사업부', '팀장', 123L, 1L);

