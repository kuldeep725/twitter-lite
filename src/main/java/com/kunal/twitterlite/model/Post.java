package com.kunal.twitterlite.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.*;

@Data
@AllArgsConstructor
public class Post implements Comparable<Post>{

    private UUID postId;
    private UUID userId;
    private String message;
    private Date createdAt;
    private Date lastModifiedAt;
    private UUID repliedTo;

    @Override
    public int compareTo(@NonNull Post other) {
        if(this.getLastModifiedAt() == null || other.getLastModifiedAt() == null)
            return 0;
        // sort in descending order of time of creation
        return other.getLastModifiedAt().compareTo(this.getLastModifiedAt());
    }

    // Obsolete
    public Map<String, String> getPostMap() {
        Map<String, String> map = new HashMap<>();
        // Using reflection api to automate the map generation of Post class
        Arrays.stream(Post.class.getDeclaredFields()).forEach(field -> {
            try {
                map.put(field.getName(), Optional.ofNullable(field.get(this)).orElse("null").toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }
}
