package jtechlog.jtechlogbootsecurity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
   public List<User> listUsers() {
        var user = SecurityContextHolder.getContext()
               .getAuthentication().getPrincipal();
       log.debug("Logged in user: {}", user);

       return userRepository.findAll(Sort.by("username"));
   }
   
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")
   public void addUser(User user) {
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       userRepository.save(user);
   }

}
