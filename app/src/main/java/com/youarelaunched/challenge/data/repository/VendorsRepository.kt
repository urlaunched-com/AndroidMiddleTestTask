package com.youarelaunched.challenge.data.repository

import com.youarelaunched.challenge.data.repository.model.Vendor

interface VendorsRepository {

    suspend fun getVendorsByCompanyName(companyName: String): List<Vendor>
    suspend fun getVendors(): List<Vendor>
}