package nl.miwnn.ch17.briljant.receptenradar.service;

import nl.miwnn.ch17.briljant.receptenradar.model.receptenRadarUser;
import nl.miwnn.ch17.briljant.receptenradar.repositories.receptenRadarUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Douwe Jan Hamersma
 */

@Service
public class receptenRadarUserService implements UserDetailsService {
    private final receptenRadarUserRepository receptenRadarUserRepository;
    private final PasswordEncoder passwordEncoder;

    public receptenRadarUserService(receptenRadarUserRepository receptenRadarUserRepository, PasswordEncoder passwordEncoder) {
        this.receptenRadarUserRepository = receptenRadarUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return receptenRadarUserRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " not found"));
    }

    public void saveUser(receptenRadarUser receptenRadarUser) {
        receptenRadarUser.setPassword(passwordEncoder.encode(receptenRadarUser.getPassword()));
        receptenRadarUserRepository.save(receptenRadarUser);
    }
}
