--liquibase formatted sql

--changeset khanhtran:issue-335-2
insert into rating (id, created_by, created_on, last_modified_by, last_modified_on, content, first_name, last_name, product_id, rating_star) values
                                                                                                                                                 (1, 'user1', NOW(), 'user1', NOW(), 'Great product!', 'John', 'Doe', 101, 5),
                                                                                                                                                 (2, 'user2', NOW(), 'user2', NOW(), 'Good value for money.', 'Jane', 'Smith', 102, 4),
                                                                                                                                                 (3, 'user3', NOW(), 'user3', NOW(), 'Satisfactory', 'Michael', 'Brown', 103, 3),
                                                                                                                                                 (4, 'user4', NOW(), 'user4', NOW(), 'Not as expected.', 'Emily', 'Johnson', 104, 2),
                                                                                                                                                 (5, 'user5', NOW(), 'user5', NOW(), 'Amazing!', 'Chris', 'Lee', 105, 5),
                                                                                                                                                 (6, 'user6', NOW(), 'user6', NOW(), 'Decent quality.', 'Anna', 'Miller', 106, 4),
                                                                                                                                                 (7, 'user7', NOW(), 'user7', NOW(), 'Could be better.', 'David', 'Garcia', 107, 3),
                                                                                                                                                 (8, 'user8', NOW(), 'user8', NOW(), 'Disappointed.', 'Sophia', 'Martinez', 108, 2),
                                                                                                                                                 (9, 'user9', NOW(), 'user9', NOW(), 'Fantastic!', 'James', 'Rodriguez', 109, 5),
                                                                                                                                                 (10, 'user10', NOW(), 'user10', NOW(), 'Quite good.', 'Oliver', 'Wilson', 110, 4),
                                                                                                                                                 (11, 'user11', NOW(), 'user11', NOW(), 'Not bad.', 'Liam', 'Moore', 111, 3),
                                                                                                                                                 (12, 'user12', NOW(), 'user12', NOW(), 'Terrible experience.', 'Sophia', 'Taylor', 112, 1),
                                                                                                                                                 (13, 'user13', NOW(), 'user13', NOW(), 'Loved it!', 'Benjamin', 'Anderson', 113, 5),
                                                                                                                                                 (14, 'user14', NOW(), 'user14', NOW(), 'Fair product.', 'Amelia', 'Thomas', 114, 3),
                                                                                                                                                 (15, 'user15', NOW(), 'user15', NOW(), 'Highly recommend!', 'Mason', 'Jackson', 115, 5),
                                                                                                                                                 (16, 'user16', NOW(), 'user16', NOW(), 'Meh.', 'Harper', 'White', 116, 2),
                                                                                                                                                 (17, 'user17', NOW(), 'user17', NOW(), 'Very satisfied.', 'Evelyn', 'Harris', 117, 5),
                                                                                                                                                 (18, 'user18', NOW(), 'user18', NOW(), 'Could improve.', 'Isabella', 'Martin', 118, 3),
                                                                                                                                                 (19, 'user19', NOW(), 'user19', NOW(), 'Wonderful!', 'Charlotte', 'Thompson', 119, 5),
                                                                                                                                                 (20, 'user20', NOW(), 'user20', NOW(), 'Not worth the price.', 'Lucas', 'Garcia', 120, 2);
