package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int id;
    private String commentText;
    private int user;
    private List<Comment> replies;
    private LocalDate dateCreated;
    private LocalDate dateModified;

    public Comment(String commentText, int user) {
        this.commentText = commentText;
        this.user = user;
        this.dateCreated = LocalDate.now();
    }
}
