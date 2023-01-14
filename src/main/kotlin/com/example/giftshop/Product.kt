package com.example.giftshop

import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = ["id"])
class Product(@Id val id: Int, val productName: String, val price: Int)