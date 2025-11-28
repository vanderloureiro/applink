<template>
    <footer class="footer">
        <p>&copy; 2025 ibira. All rights reserved.</p>
        <p>
            <nuxt-link to="/use-terms" class="terms-link">Terms of Use</nuxt-link> | 
            <nuxt-link to="/privacy-policy" class="terms-link">Privacy Policy</nuxt-link>
        </p>
    </footer>
</template>
<style scoped>
.footer {
    text-align: center;
    padding: 1em 0;
    background-color: #fff;
    color: #6b7280;
    width: 100%;
    font-size: 0.875rem;
}

.terms-link {
    color: #2563eb;
    text-decoration: none;
    font-weight: 500;
}

.terms-link:hover {
    text-decoration: underline;
}
</style>