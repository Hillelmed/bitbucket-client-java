package io.github.hillelmed.bitbucket.client.domain.admin;

import io.github.hillelmed.bitbucket.client.domain.common.Page;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The type User page.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class UserPage extends Page<User> {
}
