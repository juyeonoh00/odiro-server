


//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws IOException, ServletException {
//        System.out.println("secret" + secretKey);
//        System.out.println("doFilterInternal : 진입");
//        String accessHeader = request.getHeader(JwtProperties.ACCESS_HEADER);
//        String refreshHeader = request.getHeader(JwtProperties.REFRESH_HEADER); // 리프레시 토큰 헤더 추가
//
//        if (request.getServletPath().equals("/signin") || accessHeader == null || !accessHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
//        System.out.println("login : 진입");
//        filterChain.doFilter(request, response);
//        return;
//        }
//
//        String accessToken = accessHeader.replace(JwtProperties.TOKEN_PREFIX, "");
//
//        try {
//        // 토큰 검증
//        String username = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken)
//        .getClaim("username").asString();
//        System.out.println("???" + username);
//
//        if (username != null) {
//        Member member = memberRepository.findByNickname(username)
//        .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
//
//        PrincipalDetails principalDetails = new PrincipalDetails(member);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//        principalDetails,
//        null,
//        principalDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        } catch (TokenExpiredException e) { // 액세스 토큰 만료 예외 처리 추가
//        // 액세스 토큰이 만료된 경우 리프레시 토큰을 사용하여 새로운 액세스 토큰 발급
//        System.out.println("-------------------------------------------리프레시토큰");
//        if (refreshHeader != null && refreshHeader.startsWith(JwtProperties.TOKEN_PREFIX)) { // 리프레시 토큰 검증 추가
//        String refreshToken = refreshHeader.replace(JwtProperties.TOKEN_PREFIX, "");
//
//        try {
//        String username = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken)
//        .getSubject();
//
//        if (username != null) {
//        Member member = memberRepository.findByNickname(username)
//        .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
//
//        String newAccessToken = JWT.create()
//        .withSubject(member.getNickname())
//        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
//        .withClaim("id", member.getId())
//        .withClaim("username", member.getNickname())
//        .sign(Algorithm.HMAC512(secretKey));
//
//        // 새로운 액세스 토큰을 응답 헤더에 추가
//        response.setHeader(JwtProperties.ACCESS_HEADER, JwtProperties.TOKEN_PREFIX + newAccessToken);
//
//        PrincipalDetails principalDetails = new PrincipalDetails(member);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//        principalDetails,
//        null,
//        principalDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        } catch (JWTVerificationException ex) { // 리프레시 토큰 검증 예외 처리 추가
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Invalid refresh token");
//        return;
//        }
//        } else {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Refresh token missing or invalid");
//        return;
//        }
//        }
//        filterChain.doFilter(request, response);
//        }

