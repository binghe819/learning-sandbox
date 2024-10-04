package com.binghe

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("members")
data class Member(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("name")
    val name: String? = null,

    @Column("age")
    val age: Int? = null,

    @Column("description")
    val description: String? = null
)
