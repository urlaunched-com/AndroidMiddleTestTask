package com.youarelaunched.challenge.data.network.api

import com.youarelaunched.challenge.data.network.models.NetworkVendor

interface ApiVendors {

    suspend fun getVendorsByCompanyName(companyName: String): List<NetworkVendor>

    suspend fun getVendors(): List<NetworkVendor>
}