create table user_subscriptions(
    channel_id bigint not null,
    subscriber_id bigint     not null,
    primary key(channel_id, subscriber_id),
    FOREIGN KEY (channel_id) REFERENCES users(id),
    FOREIGN KEY(subscriber_id) REFERENCES users(id)
    )
    ENGINE=InnoDB DEFAULT CHARSET=utf8;

