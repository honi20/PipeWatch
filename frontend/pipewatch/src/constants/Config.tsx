const Config = {
  API_URL: window._env_ ? window._env_.VITE_URL : import.meta.env.VITE_URL,
  PRODUCT_LOGIN_URL: window._env_
    ? window._env_.VITE_LOGIN_URL
    : import.meta.env.VITE_LOGIN_URL,
  LOCAL_LOGIN_URL: window._env_
    ? window._env_.VITE_LOCAL_LOGIN_URL
    : import.meta.env.VITE_LOCAL_LOGIN_URL,
  NODE_ENV: window._env_
    ? window._env_.VITE_NODE_ENV
    : import.meta.env.VITE_NODE_ENV,
};

export default Config;
