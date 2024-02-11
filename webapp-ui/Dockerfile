#
# Build image.
#
FROM node:21-bookworm AS builder

WORKDIR /build

# Download dependencies.
COPY package.json package-lock.json* ./
RUN npm ci
COPY . .

# Build web application, disable NextJS telemetry facilities.
ENV NEXT_TELEMETRY_DISABLED 1
RUN  npm run build

#
# Main docker image.
#
FROM node:21-bookworm

WORKDIR /app

# Tell NodeJS to run in production mode; disable NextJS telemetry facilities.
ENV NODE_ENV production
ENV NEXT_TELEMETRY_DISABLED 1

# Create a new user and group for our server (we do not want to run as 'root').
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /build/public /app/public

# Create NextJS folder and move only required contents into it.
RUN mkdir .next
RUN chown nextjs:nodejs .next
COPY --from=builder --chown=nextjs:nodejs /build/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /build/.next/static ./.next/static

# Run server under 'nextjs' user on port 3000 (listen on all interfaces).
USER nextjs
EXPOSE 3000
ENV PORT 3000
ENV HOSTNAME "0.0.0.0"

# See https://nextjs.org/docs/pages/api-reference/next-config-js/output - NextJS produces 'server.js' build.
CMD ["node", "server.js"]