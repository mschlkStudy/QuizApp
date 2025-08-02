import axios from 'axios'

export const api = axios.create({
    baseURL: 'http://192.168.0.4:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
})

export const login = async (username, password) => {
    return api.post('/auth/login', {
        username,
        password
    })
}

export const register = async (userData) => {
    return api.post('/auth/register', {
        username: userData.username,
        password: userData.password,
        email: userData.email,
        role: 'USER'
    })
}

export const questionsApi = {
    getRandomQuestion: () => api.get('/questions/random')
}
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwt');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response Interceptor
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('jwt');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);
