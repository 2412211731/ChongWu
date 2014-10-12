package com.chongwu.bean;

import java.util.Date;

public class User {
        private Number user_id; // 用户数字ID
        private String uid; // 用户字符串ID
        private String nick; // 用户昵称
        private String sex; // 用户性别
        private UserCredit buyer_credit; // 买家信用
        private UserCredit seller_credit; // 卖家信用
        private Address location; // 用户当前居住地公开信息。如：location.city获取其中的city数据
        private Date created; // 用户注册时间
        private Date last_visit; // 最近登陆时间

        public Number getUser_id() {
                return user_id;
        }

        public void setUser_id(Number user_id) {
                this.user_id = user_id;
        }

        public String getUid() {
                return uid;
        }

        public void setUid(String uid) {
                this.uid = uid;
        }

        public String getNick() {
                return nick;
        }

        public void setNick(String nick) {
                this.nick = nick;
        }

        public String getSex() {
                return sex;
        }

        public void setSex(String sex) {
                this.sex = sex;
        }

        public UserCredit getBuyer_credit() {
                return buyer_credit;
        }

        public void setBuyer_credit(UserCredit buyer_credit) {
                this.buyer_credit = buyer_credit;
        }

        public UserCredit getSeller_credit() {
                return seller_credit;
        }

        public void setSeller_credit(UserCredit seller_credit) {
                this.seller_credit = seller_credit;
        }

        public Address getLocation() {
                return location;
        }

        public void setLocation(Address location) {
                this.location = location;
        }

        public Date getCreated() {
                return created;
        }

        public void setCreated(Date created) {
                this.created = created;
        }

        public Date getLast_visit() {
                return last_visit;
        }

        public void setLast_visit(Date last_visit) {
                this.last_visit = last_visit;
        }

        @Override
        public String toString() {
                return "{name:\"" + uid + "\", nick:\"" + nick + "\", sex:\"" + sex
                                + "\", fansnum:\"}";
        }
}

