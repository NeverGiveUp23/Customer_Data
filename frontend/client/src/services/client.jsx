import axios from 'axios';

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")} `
    }
})

export const getCustomers = async() => {
    try{
       return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`, getAuthConfig())
    } catch(error){
        throw error;
    }
}

export const saveCustomer = async(customer) => {
    try{
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`, customer)
    }catch(error){
        throw error;
    }
}

export const deleteCustomer = async (id) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/${id}`, getAuthConfig())
    } catch (error) {
        throw error;
    }
}

export const updateCustomer = async (id, update) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/${id}`, update, getAuthConfig())
    } catch (error) {
        throw error;
    }
}

export const login = async (username, password) => {
    try{
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`, username, password)
    }catch(error){
        throw error;
    }
  }
  
export const uploadCustomerPicture = async (id, formData) => {
    try {
        return axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/${id}/profile-image`,
            formData,
            {
                ...getAuthConfig(),
                'Content-Type' : 'multipart/form-data'
            }
        );
    } catch (e) {
        throw e;
    }
}

export const getCustomerProfilePictureUrl = (id) => {
    return `${import.meta.env.VITE_API_BASE_URL}/api/v1/customer/${id}/profile-image`;
}




