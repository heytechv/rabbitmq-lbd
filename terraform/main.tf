# https://www.youtube.com/watch?v=XxTcw7UTues&ab_channel=WahlNetwork

# Terraform wykona operacje aws cli (ktore wykonywalem recznie)
# na kompie automatycznie.
# Stworzy bucket, lambdy itp.

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.49"
    }
  }
  required_version = ">= 0.14.9"
}

provider "aws" {
  region            = "eu-central-1"
  access_key        = "foo"
  secret_key        = "bar"
  s3_force_path_style = true
  # lokalnie
  profile = "local"
  skip_credentials_validation = true
  skip_metadata_api_check = true
  skip_requesting_account_id = true

  endpoints {
    sqs    = "http://localhost:4566"  # adres dockera z punktu widzenia mojego kompa (nie pod-dockerow)
    s3     = "http://localhost:4566"
    lambda = "http://localhost:4566"
  }
}

# Create sqs (queue) - mam nadzieje ze if not exists
# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/sqs_queues
resource "aws_sqs_queue" "userTopic" {
  name = "userTopic"
}
resource "aws_sqs_queue" "articleTopic" {
  name = "articleTopic"
}
resource "aws_sqs_queue" "commentTopic" {
  name = "commentTopic"
}
