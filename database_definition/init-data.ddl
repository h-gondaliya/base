-- Create client table
create table client
(
    id             bigint not null
        primary key,
    client_address varchar(1000),
    client_name    varchar(255),
    email          varchar(100)
);

alter table client
    owner to "user";

-- Create stock table
create table public.netcarat_stock
(
    id                  bigint not null
        primary key,
    client_id           bigint
        constraint fksnvo3hfj3vvm6su4og1qohh2o
            references public.client,
    colour_stone_type   varchar(50),
    colour_stone_weight numeric(10, 2),
    description         varchar(500),
    design_number       varchar(50),
    diamond_pieces      integer,
    diamond_weight      numeric(10, 2),
    gold_kt             varchar(10),
    gross_weight        numeric(10, 2),
    payment_type        varchar(20),
    price               numeric(10, 2),
    product_category    varchar(10),
    sell_date           date,
    sold_price          numeric(10, 2)
);

alter table public.netcarat_stock
    owner to "user";


-- Create Approval table
create table public.approval
(
    id              bigserial
        primary key,
    approval_amount numeric(10, 2) not null,
    approval_date   date           not null,
    invoice         varchar(50),
    client_id       bigint         not null
        constraint fk7uea9e19vknj1g5g05wwua5k6
            references public.client,
    product_id      bigint         not null
        constraint fki3kqq4g72dbrhxm7jp6y2m97f
            references public.netcarat_stock
);

alter table public.approval
    owner to "user";



