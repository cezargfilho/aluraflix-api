INSERT INTO USERS(id, name, email, password) VALUES(1, 'Teste', 'teste@aluraflix.com', '$2a$12$0OIZRBX3zA10DRRv4SanmebYNBcI3Z5VJHrAPqZwMZFGeWkmCMVNu');
INSERT INTO USERS(id, name, email, password) VALUES(2, 'Admin', 'admin@aluraflix.com', '$2a$12$BgeKcSzYAq9lMrqI8nEfvuuGPVdL.IPslw.Qi4QZ93LP99snU3RW2');

INSERT INTO PROFILE(id, name) VALUES(1, 'ROLE_USER');
INSERT INTO PROFILE(id, name) VALUES(2, 'ROLE_ADMIN');
 
INSERT INTO USERS_PROFILES(user_id, profiles_id) VALUES(1, 1);
INSERT INTO USERS_PROFILES(user_id, profiles_id) VALUES(2, 2);

INSERT INTO CATEGORY(id, title, color) VALUES(1, 'LIVRE','#AAAAAA');
INSERT INTO CATEGORY(title, color) VALUES('PROGRAMACAO','#2DBF13');
INSERT INTO CATEGORY(title, color) VALUES('FRONT-END','#A90A00');
INSERT INTO CATEGORY(title, color) VALUES('DATA SCIENCE','#FFF700');
INSERT INTO CATEGORY(title, color) VALUES('DESIGN','#0049FF');

INSERT INTO VIDEO(title, description, url, category_id) VALUES('React casa 50 MILHÕES', 'React de casas milionarias', 'https://youtu.be/p31HeB2_7eI', 2);
INSERT INTO VIDEO(title, description, url, category_id) VALUES('CASIMIRO REAGE À RESPOSTA DE MARCELO | Cortes do Casimito', 'Live do dia 18/07/2021 para o dia 19/07/2021', 'https://youtu.be/0AylNJxkjCg', 3);
INSERT INTO VIDEO(title, description, url, category_id) VALUES('CASIMIRO VAI A LOUCURA COM REGRA DA CBF | Cortes do Casimito', 'Live do dia 18/07/2021 para o dia 19/07/2021', 'https://youtu.be/GgLPAmbiLkE', 4);
INSERT INTO VIDEO(title, description, url, category_id) VALUES('QUANTAS HORAS UM ADULTO PRECISA DORMIR POR DIA? | Cortes do Casimito', 'Live do dia 14/07/2021 para o dia 15/07/2021', 'https://youtu.be/5ZngG4Rcx8U', 4);
INSERT INTO VIDEO(title, description, url, category_id) VALUES('GOLS DA RODADA BRASILEIRAO', 'Todos os gols da rodada do brasileirao 2021', 'http://youtube.com', 1);