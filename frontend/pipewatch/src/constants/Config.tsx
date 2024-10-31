const Config = {
  backendUrl: window._env_
    ? window._env_.VITE_BACKEND_URL
    : import.meta.env.VITE_BACKEND_URL,
};

export default Config;
