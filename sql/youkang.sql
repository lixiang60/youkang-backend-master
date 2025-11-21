CREATE TABLE yk_customer_info (
                                  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
                                  customer_name VARCHAR(100) COMMENT '客户姓名',
                                  region VARCHAR(100) COMMENT '地区',
                                  address VARCHAR(255) COMMENT '地址',
                                  phone VARCHAR(20) COMMENT '电话',
                                  email VARCHAR(100) COMMENT '邮箱',
                                  wechat_id VARCHAR(50) COMMENT '微信ID',
                                  customer_level VARCHAR(50) COMMENT '等级',
                                  status VARCHAR(50) COMMENT '状态',
                                  sales_person VARCHAR(100) COMMENT '销售员',
                                  customer_unit VARCHAR(100) COMMENT '客户单位',
                                  payment_method VARCHAR(100) COMMENT '结算方式',
                                  invoice_type VARCHAR(100) COMMENT '发票种类',
                                  remarks TEXT COMMENT '备注',
                                  create_by VARCHAR(100) COMMENT '添加人',
                                  create_time DATETIME COMMENT '添加时间',
                                  update_by VARCHAR(100) COMMENT '更新人',
                                  update_time DATETIME COMMENT '更新时间',
                                  company VARCHAR(100) COMMENT '所属公司'
);
CREATE TABLE yk_subject_group_info (
                               id INT PRIMARY KEY AUTO_INCREMENT COMMENT '课题组id',
                               name VARCHAR(100) COMMENT '名称',
                               region VARCHAR(100) COMMENT '地区',
                               sales_person VARCHAR(100) COMMENT '业务员',
                               payment_method VARCHAR(100) COMMENT '结算方式',
                               invoice_title VARCHAR(100) COMMENT '发票抬头',
                               company_id VARCHAR(50) COMMENT '所属公司ID',
                               contact_person VARCHAR(100) COMMENT '联系人',
                               contact_phone VARCHAR(20) COMMENT '联系电话',
                               contact_address VARCHAR(255) COMMENT '联系地址',
                               create_time DATETIME COMMENT '添加时间',
                               create_by VARCHAR(100) COMMENT '添加人',
                               points_base INT COMMENT '积分基数',
                               is_reminder BOOLEAN COMMENT '是否提醒',
                               reminder_content TEXT COMMENT '提醒内容',
                               gene_tag_type VARCHAR(100) COMMENT '基因标签类型',
                               tag_template VARCHAR(255) COMMENT '标签模板',
                               coa_template TEXT COMMENT 'COA模板'
);

create table yk_customer_subject(
    customer_id int COMMENT '客户ID',
    subject_group_id int comment '课题组id'
);