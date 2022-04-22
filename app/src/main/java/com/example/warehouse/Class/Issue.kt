package com.example.warehouse.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="issues")
data class Issue(@PrimaryKey val IssueID: Int,
                     val IssueContent: String,
                     val SerialNo: String) {
}