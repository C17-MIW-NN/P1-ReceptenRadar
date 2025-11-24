package nl.miwnn.ch17.briljant.receptenradar.service;

import nl.miwnn.ch17.briljant.receptenradar.model.ReceptenRadarUser;
import nl.miwnn.ch17.briljant.receptenradar.repositories.ReceptenRadarUserRepository;
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
    private final ReceptenRadarUserRepository receptenRadarUserRepository;
    private final PasswordEncoder passwordEncoder;

    public receptenRadarUserService(ReceptenRadarUserRepository receptenRadarUserRepository, PasswordEncoder passwordEncoder) {
        this.receptenRadarUserRepository = receptenRadarUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return receptenRadarUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " not found"));
    }

    public void saveUser(ReceptenRadarUser receptenRadarUser) {
        receptenRadarUser.setPassword(passwordEncoder.encode(receptenRadarUser.getPassword()));
        receptenRadarUserRepository.save(receptenRadarUser);
    }
}
