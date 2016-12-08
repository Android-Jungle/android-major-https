package com.jungle.easyhttp.demo;

import java.util.List;

public class GithubUserInfo {

    public static class Project {
        public String name;
        public String url;
    }

    public String uid;
    public String userName;
    public String site;
    public String[] languages;
    public List<Project> projects;
}
