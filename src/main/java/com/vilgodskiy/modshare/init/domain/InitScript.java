package com.vilgodskiy.modshare.init.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Entity for init classes - persist information about executed init-classes (init-script)
 */
@Entity
@Table(name = "mds_init_script")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InitScript {

    /**
     * Class name
     */
    @Id
    @Column(name = "s_name", unique = true)
    private String name;

    /**
     * Executed date
     */
    @Column(name = "d_exec_date")
    private LocalDateTime execDate;
}
