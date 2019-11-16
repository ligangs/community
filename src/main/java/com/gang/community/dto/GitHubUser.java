package com.gang.community.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private String login;
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
