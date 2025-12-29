// UserRepository.java (Adding Index for fast lookups)


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

// We need an index on PIN because we use it with accountNumber in lookups
public interface UserRepository extends MongoRepository<User, String> {
    
    // This is the primary lookup method for login. 
    // MongoDB will use the index on 'accountNumber' (@Id) AND a potential index on 'pin'.
    Optional<User> findByAccountNumberAndPin(
        @Param("accountNumber") String accountNumber, 
        @Param("pin") String pin
    );
}