-- user
INSERT INTO "user"(user_id, email, password, name, state, role, uuid)
VALUES (123L, 'test@ssafy.com', '$2a$10$W9WYGcUhi6E2NxqnofecW.DEsMkr42YiOSM8Ou/UEQZsMD3WHL8uy', '테스트', 'PENDING',
        'ROLE_EMPLOYEE',
        '1604b772-adc0-4212-8a90-81186c57f598'),
       (124L, 'pipewatch_admin@ssafy.com', '$2a$10$W9WYGcUhi6E2NxqnofecW.DEsMkr42YiOSM8Ou/UEQZsMD3WHL8uy', 'paori',
        'ACTIVE', 'ROLE_ENTERPRISE',
        '1604b772-adc0-4212-8a90-81186c57f599'),
       (125L, 'choi@ssafy.com', '$2a$10$W9WYGcUhi6E2NxqnofecW.DEsMkr42YiOSM8Ou/UEQZsMD3WHL8uy', '최싸피', 'ACTIVE',
        'ROLE_EMPLOYEE',
        '1604b772-adc0-4212-8a90-81186c57f600'),
       (126L, 'kim@ssafy.com', '$2a$10$W9WYGcUhi6E2NxqnofecW.DEsMkr42YiOSM8Ou/UEQZsMD3WHL8uy', '김싸피', 'ACTIVE',
        'ROLE_ADMIN',
        '1604b772-adc0-4212-8a90-81186c57f601'),
       (127L, 'pipewatch_admin@samsung.com', '$2a$10$W9WYGcUhi6E2NxqnofecW.DEsMkr42YiOSM8Ou/UEQZsMD3WHL8uy', 'samsung',
        'ACTIVE', 'ROLE_ENTERPRISE',
        '1604b772-adc0-4212-8a90-81186c57f602')
;

-- enterprise
INSERT INTO enterprise(enterprise_id, name, industry, manager_email, manager_phone_number, is_active, user_id)
VALUES (1L, 'paori', '제조업', 'admin@ssafy.com', '010-1234-5678', true, 124L),
       (2L, 'samsung', '제조업', 'admin@samsung.com', '010-1234-5678', true, 127L);

-- employeeInfo
INSERT INTO employee_info(employee_info_id, emp_no, department, emp_class, user_id, enterprise_id)
VALUES (123L, 1243242L, 'IT사업부', '팀장', 123L, 1L),
       (124L, 1534534L, '마케팅부', '대리', 125L, 1L),
       (125L, 1423435L, '인사부', '부장', 126L, 1L)
;

-- waiting
INSERT INTO waiting(waiting_id, user_id, role)
VALUES (1L, 123L, 'ROLE_EMPLOYEE');

-- building and floor
INSERT INTO building_and_floor(building_floor_id, enterprise_id, name, floor)
VALUES (1L, 1L, '역삼 멀티캠퍼스', 14),
       (2L, 1L, '역삼 멀티캠퍼스', 12),
       (3L, 1L, '부울경 멀티캠퍼스', 1),
       (4L, 1L, '부울경 멀티캠퍼스', 3)
;