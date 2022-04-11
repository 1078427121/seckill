load data local infile 'D:/study/bytecamp2019/uid_sessionkey_student win.txt'
into table user(uid,sessionid);
load data local infile 'D:/tools/data1.txt'
into table product_info(pid,price,detail);