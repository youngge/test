package com.example.yang.test.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class UserBean {

    /**
     * _id : 1
     * name : Tom
     * sex : 男
     * phone : 10086
     * job : 学生
     * address : 广东省广州市天河区科韵路
     * age : 23
     */

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String _id;
        private String name;
        private String sex;
        private String phone;
        private String job;
        private String address;
        private String age;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "_id='" + _id + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", phone='" + phone + '\'' +
                    ", job='" + job + '\'' +
                    ", address='" + address + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }
}
