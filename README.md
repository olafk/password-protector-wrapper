# Liferay Default Password Protector

I get it - due to Covid19 everybody is at home in their own network, so this basic OpSec Plugin doesn't do much until you get back out into the wild. But when you get back into foreign networks, this plugin will have protected all new accounts in your system:

Liferay's configured default password "test" is quite insecure. One of my life-goals is to distribute Raspberry Pis to all offices that crawls all devices' port 8080, tries to log in with default credentials and defaces the homepage. (If your homepage gets defaced: Wasn't me - sorry for spreading the idea around, someone else preempted me)

If you keep the default administrator "test@liferay.com" with the default password "test", anyone logging in to your system with these permissions will be able to install any plugin on your system, and change all content.
Unfortunately this is a situation that I commonly see, even though most often the choice is made explicitly in the Setup Wizard. 

But there are other situations, e.g. automatically imported Demo data (Looking at the Commerce Demo Accellerators) that create user accounts with password "test" (including Administrator accounts).

## How to use this plugin?

Just build the plugin (checkout in a Liferay Workspace, tested with `liferay.workspace.product=dxp-7.3-ga1` - earlier versions were built on Target Platform 7.2.10.1) and deploy it in your system. It will eliminate all new uses of "test" as a password and replace them with your configured `default.admin.password`. If you didn't configure this value different than "test", a hashed random value is being used. The random seed is different on each portal start, but will stay consistent for all uses during the same uptime. If you run a cluster, no cluster communication is implemented, every node has its own random seed.

If you *want* to *see* the generated password: It's logged in `com.liferay.opsec.eliminatedefaultpasswordonceandforall.PasswordSanitizerImpl` on level `info`.

So: If you want to set the new default password yourself, configure it in your portal-ext.properties as `default.admin.password`. This means that it's on your disk, in clear text. Or just accept a random password - that's fine if you use impersonation in your demos instead of explicitly signing in.

## Side effect

All the faked API calls that still use Basic Authentication with "test@liferay.com/test" will break. Nice - finally they'll need to be implemented properly. ;)

 


