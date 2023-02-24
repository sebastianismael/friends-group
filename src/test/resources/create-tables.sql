USE test;

create table friends_group(
      id INT NOT NULL AUTO_INCREMENT,
      name VARCHAR(100) NOT NULL,
      PRIMARY KEY ( id )
);

create table user(
     id INT NOT NULL AUTO_INCREMENT,
     name VARCHAR(100) NOT NULL,
     friends_group_id INT,
     FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
     PRIMARY KEY ( id )
);

create table shared_expenses(
    id INT NOT NULL AUTO_INCREMENT,
    friends_group_id INT NOT NULL,
    owner INT NOT NULL,
    amount FLOAT NOT NULL,
    detail VARCHAR(100) NOT NULL,
    status VARCHAR(10) NOT NULL,
    date DATETIME,
    FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
    FOREIGN KEY (owner) REFERENCES user(id),
    PRIMARY KEY ( id )
);

create table payment(
    id INT NOT NULL AUTO_INCREMENT,
    payer INT NOT NULL,
    expent_id INT NOT NULL,
    amount FLOAT NOT NULL,
    FOREIGN KEY (payer) REFERENCES user(id),
    FOREIGN KEY (expent_id) REFERENCES shared_expenses(id),
    PRIMARY KEY ( id )
);
-- brew install rbenv/tap/openssl@1.0
-- ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2t /usr/local/opt/openssl