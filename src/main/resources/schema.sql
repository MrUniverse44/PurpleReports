create table if not exists `reports` (
    `reportId` int not null auto_increment,
    `reportedPlayer` varchar(36) not null,
    `reporter` varchar(36) not null,
    `reason` varchar(36) not null,
    primary key (`reportId`)
);