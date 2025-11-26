package nl.miwnn.ch17.briljant.receptenradar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Douwe Jan Hamersma
 * Any user of the ReceptenRadar software.
 */

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReceptenRadarUser implements UserDetails {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "recepten_radar_user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<Recipe> likedRecipes;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public Set<Recipe> getLikedRecipes() {
        return likedRecipes;
    }

    public void setLikedRecipes(Set<Recipe> likedRecipes) {
        this.likedRecipes = likedRecipes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
