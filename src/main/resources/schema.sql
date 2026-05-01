create table if not exists agent_task (
    id varchar(36) primary key,
    user_query varchar(2000) not null,
    intent varchar(80),
    status varchar(40) not null,
    final_answer text,
    error_message varchar(2000),
    metadata_json text,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table if not exists agent_step (
    id varchar(36) primary key,
    task_id varchar(36) not null,
    step_order integer not null,
    step_name varchar(200) not null,
    tool_name varchar(120),
    status varchar(40) not null,
    params_json text,
    result_json text,
    error_message varchar(2000),
    started_at timestamp,
    finished_at timestamp,
    created_at timestamp not null,
    updated_at timestamp not null,
    constraint fk_agent_step_task foreign key (task_id) references agent_task(id)
);

create index if not exists idx_agent_step_task_id on agent_step(task_id);
