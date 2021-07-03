# SpigotProxy

SpigotProxy is a Spigot plugin that enables compatibility with HAProxy's PROXY v2 protocol.


## What's HAProxy?

[HAProxy](http://www.haproxy.org/) is a high performance, open-source Load Balancer and Reverse Proxy for TCP-based services.


## What's the PROXY protocol?

The PROXY protocol was designed to transmit a HAProxy client's connection information to the destination through other proxies or NAT routing (or both). HAProxy must be configured to send PROXY packets to your Spigot backend(s) in the `backend` or `server` configuration using the `send-proxy-v2` option.


## Installing HAProxy 1.5 / 1.6 on Ubuntu or Debian

HAProxy 1.5 or above is required to use the PROXY protocol.

Please see [https://haproxy.debian.net](https://haproxy.debian.net) for instructions on how to obtain a more recent package of HAProxy on Ubuntu or Debian.

## Example configuration

<pre>
listen minecraft
	bind :25565
	mode tcp
	balance leastconn
	option tcp-check
	server minecraft1 192.168.0.1:25565 check-send-proxy check send-proxy-v2
	server minecraft2 192.168.0.2:25565 check-send-proxy check send-proxy-v2
	server minecraft3 192.168.0.3:25565 check-send-proxy check send-proxy-v2
	server minecraft4 192.168.0.4:25565 check-send-proxy check send-proxy-v2
	server minecraft5 192.168.0.5:25565 check-send-proxy check send-proxy-v2
	server minecraft6 192.168.0.6:25565 check-send-proxy check send-proxy-v2
	server minecraft7 192.168.0.7:25565 check-send-proxy check send-proxy-v2
	server minecraft8 192.168.0.8:25565 check-send-proxy check send-proxy-v2
</pre>


## How do I know it's working?

In the Spigot server (with late-bind on false), you should see the actual IP of players connecting through HAProxy.

## Other uses:
- With the [nginx stream module](https://docs.nginx.com/nginx/admin-guide/load-balancer/tcp-udp-load-balancer/) with `proxy_protocol on`.
- With [infrared](https://github.com/haveachin/infrared), a minecraft focused lightweight reverse proxy written in Go.

## Note

This plugin is inspired by [/MinelinkNetwork/BungeeProxy](https://github.com/MinelinkNetwork/BungeeProxy). Go to that repo to see the bungee version of this plugin