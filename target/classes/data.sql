insert into role(role_id, role) values (1, "USER");
insert into role(role_id, role) values (2, "ADMIN");
insert into user (user_id, document, email, name, password, active, image) values (1, "1111111", "luizfelipebasile@gmail.com", "Luiz", "$2a$10$ehMUQhsfXf/KJX4J6CvODeapbu9clyOwVC41TS4TqyvNO4HGPSAcm", 1, "rejoed05uyghymultqsv");
insert into user_roles (usuarios_user_id, roles_role_id) values(1,2);
insert into user_roles (usuarios_user_id, roles_role_id) values(1,1);
