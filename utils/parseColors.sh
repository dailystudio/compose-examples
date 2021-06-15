#!/bin/bash

function print_usage {
  echo "Usage:"
  echo
  echo "  $0 [-options] -f ANDROID_COLORS_FILE -p TARGET_PACKAGE"
  echo "    This script will deploy a application to run as a service after boot"
  echo ""
  echo "    -f ANDROID_COLORS_FILE:				the colors.xml to be convert"
  echo "    -p TARGET_PACKAGE:          		the target package of Jetpack Compose Color class"
  echo "    -o OUTPUT_FILE:          			the output file which contains Jetpack Compose colors"
  echo
}

function trim {
    echo "$1" | sed -n '1h;1!H;${;g;s/^[ \t]*//g;s/[ \t]*$//g;p;}'
}

function exit_abnormal {
	print_usage
	exit 1
}

output_filename="Color.kt"
output_file="./${output_filename}"
while getopts :f:p:o:hH opt; do
  case ${opt} in
    f)
	    file=${OPTARG}
    	;;
    p)
	    package=${OPTARG}
    	;;
    o)
	    output_file=${OPTARG}
    	;;
    h|H)
      print_usage
      exit 2
      ;;
    :)
	  echo "[ERROR] $0: -${OPTARG} requires an argument."
      exit_abnormal
      ;;
    *)
	  echo "[ERROR] $0: -${OPTARG} is unsuppported."
      exit_abnormal
      ;;
  esac
done

if [ -z "${file}" ] || [ -z "${package}" ]; then
    echo "[ERROR] required options is missing."
    exit_abnormal
fi

echo "[Android]"
echo "colors.xml: ${file}"
echo ""
echo "[Jetpack Compose]"
echo "Package: ${package}"
echo "Output file: ${output_file}"

echo "package ${package}" > ${output_file}
echo "" >> ${output_file}
echo "import androidx.compose.ui.graphics.Color" >> ${output_file}
echo "" >> ${output_file}
cat ${file} | while read line; do
	if [[ ${line} == *"<color name="* ]]; then
		echo ${line} | sed "s/<color\ name=\"/val\ /g" | sed "s/\">#/ = Color(0xff/g" | sed "s/<\/color>/)/g" >> ${output_file}
	fi
done
echo "" >> ${output_file}

echo ""
