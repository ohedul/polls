package bd.ohedulalam.polls.security;

import bd.ohedulalam.polls.model.User;
import bd.ohedulalam.polls.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(s, s)
                .orElseThrow(()->new UsernameNotFoundException("No user found by this username or email!"));
        return UserPrincipal.create(user);
    }

    //This method is used by JwtAuthenticationFilter.

    @Transactional
    public UserDetails loadUserById(long id){
        User user = userRepository.findById(id).orElseThrow(()-> new
                UsernameNotFoundException("User not found with id: " + id));
        return UserPrincipal.create(user);
    }
}
