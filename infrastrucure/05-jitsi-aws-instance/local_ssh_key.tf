resource "hcloud_ssh_key" "jitsi_ssh_key" {
  name       = "${var.module}_ssh_key"
  public_key = file("~/.ssh/id_rsa.pub")
}
