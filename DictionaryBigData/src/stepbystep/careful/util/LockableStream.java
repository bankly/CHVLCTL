package stepbystep.careful.util;

public interface LockableStream {
	/***
	 * Lock the shared advisory lock. This method will create a shared "read" lock when the shared
	 * argument is true and an exclusive "write" lock when the shared argument is false. If the
	 * block argument is false then this call will not block if the lock can't be obtained.
	 */
	public boolean lock(boolean shared, boolean block);

	/***
	 * Unlock the advisory lock. Return true if a lock was held, false otherwise.
	 */
	public boolean unlock();

}
