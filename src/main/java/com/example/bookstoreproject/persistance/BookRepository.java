package com.example.bookstoreproject.persistance;

import com.example.bookstoreproject.persistance.entity.AuthorEntity;
import com.example.bookstoreproject.persistance.entity.BookEntity;
import com.example.bookstoreproject.persistance.entity.PublisherEntity;
import com.example.bookstoreproject.util.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Optional<BookEntity> findByIsbn(int isbn);

    @Query("select b.imageUrlL from BookEntity b")
    List<String> getUrls();


    //Show the name, surname of an author who have at least one book published

    //  @Query("select new AuthorEntity(u.id, a.totalBookCount) u.name, u.surname from UserEntity u join AuthorEntity a on " +
    @Query("select a from AuthorEntity a " +
            "left join  a.user u " +
            "where a.totalBookCount > 1 group by a.user.id")
    List<AuthorEntity> getAllAuthorsWithBooks();

    //Show a publisher and number of sub resources it has.
    @Query("SELECT new com.example.bookstoreproject.util.KeyValue(p, COUNT(p.id)) " +
            "FROM BookEntity b " +
            "left join b.publisher p")
    List<KeyValue<PublisherEntity, Integer>> getAllPublishersAndResources();

    // Show author id and number of resources written by him/her
  /*  @Query("SELECT new com.example.bookstoreproject.util.KeyValue (a , COUNT(b.id)) " +
            "FROM BookEntity b " +
            "left join b.authorName a " +
            "GROUP by AuthorEntity.id")
    List<KeyValue<AuthorEntity, Integer>> getAllAuthorsAndResources();*/

    // Show all the book ISBNs which have been rated at least once
    @Query("SELECT b.isbn FROM BookEntity b " +
            "JOIN RatingEntity r ON r.book.id = b.id " +
            "GROUP BY b.isbn " +
            "HAVING COUNT(b.id) >= 1")
    List<Integer> getAllRatedBooks();
}
